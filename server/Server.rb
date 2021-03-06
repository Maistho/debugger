#!/usr/bin/env ruby

require 'socket'
require 'uri'
require 'json'
require 'logger'
require 'open3'

#TODO: Move global var into problemDB
LANG = {'p' => 'PYTHON2', 'PYTHON2' => 'p'}
DIFF = {'0' => "EASY", 'EASY' => '0'}

class Reception
	def initialize
		@problemDB = ProblemDB.new
		@playerDB = ScorePlayerDB.new
		@testServer = TestServer.new @problemDB
		#TODO: Create helpfunction for logging
		#TODO: Set logging level from commandline start
		@log = Logger.new STDERR
		@log.level = Logger::DEBUG
	end

	def run port
		begin
			# Start our server to handle connections (will raise things on errors)
			@socket = TCPServer.new port
			loop do
				# Handle every request in another thread
				s = @socket.accept
				Thread.new s, &method(:handle_request)
			end
		rescue Interrupt
			# CTRL-C
			@log.add Logger::INFO, 'Got Interrupt'
		ensure
			# Ensure that we release the socket on errors
			if @socket
				@socket.close
				@log.add Logger::DEBUG, 'Socket closed'
			end
			@testServer.clean
			@log.add Logger::INFO, 'Shut down'
		end
	end

	def handle_request from_client
		@log.add Logger::DEBUG, "New Request"
		incoming = ""
		loop do
			line = from_client.readline
			if line.strip.empty?
				break
			else
				incoming << line
			end
		end
		jsoninc = JSON.parse(incoming)
		@log.add Logger::DEBUG, jsoninc

		response = {}
		#TODO: Manage response creating in a better way
		case jsoninc['method']
		when "postBug"
			#Posting a solution
			@log.add Logger::DEBUG, "Testing bug"
			response = @testServer.trySolution(jsoninc['id'],jsoninc['code'])

		when "getBug"
			#Requesting a problem
			# If id == nil, get random problem
			if jsoninc['id'].nil?
				@log.add Logger::DEBUG, "Getting random"
				response = @problemDB.getProblem(@problemDB.getRandom(jsoninc['language'],jsoninc['difficulty']))
			else
				@log.add Logger::DEBUG,  "Getting from ID"
				response = @problemDB.getProblem(jsoninc['id'])
			end

		when "getScores"
			#Fetch scores for client of ID
			@log.add Logger::DEBUG, "getScores - NYI"
			response = @playerDB.getScores(jsoninc['player_id'])

		when "getLeaderboard"
			#Fetch leaderboard for problem
			@log.add Logger::DEBUG, "getLeaderboard - NYI"
			response = @playerDB.getLeaderboard(jsoninc['id'])

		else
			@log.add Logger::DEBUG, "Failure to apprehend call"
			#TODO: Write error to client
		end

		buff = response.to_json + "\r\n\r\n"
		@log.add Logger::DEBUG, "Responding:"
		@log.add Logger::DEBUG, response
		from_client.write(buff)
		# Close the sockets
		from_client.close
		@log.add Logger::DEBUG, "Closed Socket"
	end
end

class ProblemDB
	#TODO: Remake ID standard
	#TODO: Move entire DB system to external database
	#ID-standard: lang, difficulty, 3-digit ID; Example: p1001.
	#	The 3-digit ID is unique for each language and difficulty. Ie p1000 and p2000 are two separate problems.
	def initialize
		@dbpath = Dir.pwd + "/dbroot/"
		puts "dbpath is " +@dbpath
		@ext = {'p' => 'py'}
		loadDB
		@log = Logger.new STDERR
	end

	#Loads/reloads the database.
	def loadDB
		@db = {}
		lang = ""
		diff = ""
		File.open("#{@dbpath}root.txt").each do |line|
			if line[0] != "#" && line[0] != "\t"
				lang = LANG[line[0]]
				@db[lang] = {}
			elsif line[0] == "\t" && line[1] != "\t"
				diff = DIFF[line[1]]
				@db[lang][diff] = []
			elsif line[0] == "\t" && line[1] == "\t"
				@db[lang][diff].push(line[2..-2])
			end
		end
	end

	#Inspect the database Hash, mostly for error-shooting.
	def inspectDB
		puts "Database: #@db"
	end

	#Checks if the file with the corresponding ID is listed in the database file. Unlisted but existing files are treated as non-existent until added to the database.
	def fileListed(id)
		return @db[LANG[id[0]]][DIFF[id[1]]].include? id[2..-1]
	end

	def getRandom language, difficulty
		return LANG[language] + DIFF[difficulty] + @db[language][difficulty].sample
	end

	def getTest id
		response = {}
		if !(path = getTestPath(id)).nil?
			response['test'] = readFile path
			response['id'] = id
			response['language'] = LANG[id[0]]
		end
		return response
	end

	def getProblem id
		response = {}
		if !(path = getProblemPath(id)).nil?
			response['code'] = readFile path
			response['id'] = id
			response['language'] = LANG[id[0]]
		end
		return response
	end

	def getHints id
		response = {}
		if !(path = getHintsPath(id)).nil?
			response['hints'] = readFile(path).split("\n")
			response['id'] = id
			response['language'] = LANG[id[0]]
		end
		return response
	end

	def getTestPath id
		return fileListed(id) ? @dbpath + "#{id[0]}/#{id[1]}/#{id[2..4]}/test.#{@ext[id[0]]}" : nil
	end

	def getProblemPath id
		return fileListed(id) ? @dbpath + "#{id[0]}/#{id[1]}/#{id[2..4]}/main.#{@ext[id[0]]}" : nil
	end

	def getHintsPath id
		return fileListed(id) ? @dbpath + "#{id[0]}/#{id[1]}/#{id[2..4]}/hints.txt" : nil
	end

	def readFile path
		response = ""
		File.open(path, "r") {|f| response << f.read}
		return response
	end

end

class TestServer
	#TODO: manage directories in a better way
	def initialize db
		@directories = ["test01","test02","test03","test04","test05","test06"]
		@stddirs = @directories
		@db = db
		@logger = Logger.new STDERR
		@logger.level = Logger::DEBUG
	end
	def clean
		@directories.each {|d| %x(rm #{d}/*)}
		@directories.each {|d| %x(rmdir #{d})}
	end


	def get_dir
		if @directories.empty?
			return nil
		else
			return "/home/test/" + @directories.pop
		end
	end

	def return_dir dir
		@directories.push dir.split('/').last
	end

	def log error
		@logger.add Logger::ERROR, error
	end

	#Tries a solution and returns the output
	#TODO: Better error logs
	def trySolution(id, code)
		test_path = @db.getTestPath(id)
		test_file = test_path.split('/').last
		solution_file = "solution." + test_file.split('.').last
		while (dir = get_dir).nil?
			sleep 5
		end
		err = %x(mkdir #{dir})
		if err == ""
			File.open("#{dir}/#{solution_file}",'w') {|f| f.write(code)}
			%x(cp #{test_path} #{dir}/)

			output = ""
			stdin, stdout, stderr = Open3.popen3("python #{dir}/#{test_file}")
			output << stderr.read.split("\n").last(2).join("\n")
			output << "\n"
			output << stdout.read

			response = {}
			response['output'] = output
			response['id'] = id
			return response
		else
			log err
			return {}
		end
	ensure
		if !dir.nil?
			%x(rm #{dir}/*)
			err = %x(rmdir #{dir})
			if err != ""
				log err
			end
			return_dir(dir)
		end
	end
end

class ScorePlayerDB
	#TODO: Implement using a relation database
	def initialize
	end

	def getScores()
		puts "pScores found"
		return ""
	end

	def getLeaderboard(problem_id)
		puts "pLeaderboard found"
		return ""
	end
end

# Get parameters and start the server
#TODO: Better argument management, including more options like debug and such
if __FILE__ == $0
	if ARGV.empty?
		port = 8008
	elsif ARGV.size == 1
		port = ARGV[0].to_i
	else
		puts 'Usage: ServerCom.rb [port]'
		exit 1
	end
	Reception.new.run port
end

