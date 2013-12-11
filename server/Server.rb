#!/usr/bin/env ruby

require 'socket'
require 'uri'
require 'json'
require 'logger'

class Reception
	def initialize
		@problemDB = ProblemDB.new
		@playerDB = ScorePlayerDB.new
		@testServer = TestServer.new @problemDB
		@log = Logger.new STDERR
	end

	def run port
		begin
			# Start our server to handle connections (will raise things on errors)
			@socket = TCPServer.new port
			# Handle every request in another thread
			loop do
				s = @socket.accept
				Thread.new s, &method(:handle_request)
			end
			# CTRL-C
		rescue Interrupt
			@log << 'Got Interrupt..'
			# Ensure that we release the socket on errors
		ensure
			if @socket
				@socket.close
				@log << 'Socket closed..'
			end
			@log << 'Quitting.'
		end
	end

	def handle_request from_client

		#convert request into function call by case
		#pProblem(id,code=string)
		#pSolution(id, bool, [achievements,..,..],response=str)
		#pScores(id,[uid,score=int])
		#pLeaderboard([uid,score=int])

		incoming = ""

		loop do
			line = from_client.readline
			if line.strip.empty?
				break
			else
				incoming << line
			end
		end

		@log << incoming

		jsoninc = JSON.parse(incoming)

		response = ""

		case jsoninc['method']
			#Posting a solution
		when "Post"
			@log << "D"
			response = @testServer.trySolution(jsoninc['id'],jsoninc['code'])

			#Requesting a random problem of x difficulty & y language
		when "randReq"
			@log << "R"
			response = @problemDB.getRandom(jsoninc['language'],jsoninc['difficulty'])

			#Requesting a problem of ID
		when "idReq"
			@log << "I"
			response = @problemDB.getProblem(jsoninc['id'])

			#Fetch scores for client of ID
		when "getScores"
			@log << "S"
			response = @playerDB.getScores(jsoninc['player_id'])

			#Fetch leaderboard for problem
		when "getLeaderboard"
			@log << "L"
			response = @playerDB.getLeaderboard(jsoninc['id'])

		else
			@log << "Failure to apprehend call"
			#TODO: Write error to client
		end

		#TODO: Write proper response

		buff = response + "\r\n\r\n"
		@log << "responding"
		from_client.write(buff)
		@log << "conn_end"
		# Close the sockets
		from_client.close
	end
end

class ProblemDB
	#ID-standard: lang, difficulty, 3-digit ID; Example: p1001.
	#	The 3-digit ID is unique for each language and difficulty. Ie p1000 and p2000 are two separate problems.
	def initialize
		@dbpath = Dir.pwd + "/dbroot/"
		@ext = {'p' => 'py'}
		loadDB
	end

	#Loads/reloads the database.
	def loadDB
		@db = Hash.new
		lang = ""
		diff = 0
		File.open("#{@dbpath}root.txt").each do |line|
			if line[0] != "#" && line[0] != "\t"
				lang = line[0]
				@db[lang] = []
			elsif line[0] == "\t" && line[1] != "\t"
				diff = line[1].to_i
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
		return @db[id[0]][id[1].to_i].include? id[2..-1]
	end
=begin
	#Finds the file with the corresponding ID, given that it's listed in the database, and return it's content as a string.
	def retrieveFile(id)
		path = retriveFilePath(id)
		fileContent = ""
		File.open(path).each do |line|
			fileContent += line
		end
		return fileContent
	end

	def retriveFilePath(id)
		path = Dir.pwd
		#	TODO: Dynamic file-ending handling, rather than a hardcoded solution.
		if self.fileListed(id) == true
			if id[2] == "p"
				path = path + "/dbroot/#{id[0]}/#{id[1]}/#{id[3..5]}/main.py"
			elsif id[2] == "s"
				path = path + "/dbroot/#{id[0]}/#{id[1]}/#{id[3..5]}/test.py"
			else
				path = path + "/dbroot/#{id[0]}/#{id[1]}/#{id[3..5]}/hints.txt"
			end
			return path
		else
			return nil
		end
	end
=end

	def getRandom language, difficulty
		response = ""
		File.open(getRandomPath(language, difficulty), "r") {|f| response << f.read}
		return response
	end
	def getTest language, difficulty
		response = ""
		File.open(getTestPath(language, difficulty), "r") {|f| response << f.read}
		return response
	end
	def getProblem language, difficulty
		response = ""
		File.open(getProblemPath(language, difficulty), "r") {|f| response << f.read}
		return response
	end
	def getHints language, difficulty
		response = ""
		File.open(getHintsPath(language, difficulty), "r") {|f| response << f.read}
		return response
	end

	def getRandomPath language, difficulty
		return Dir.glob(@dbpath + language + "/" + difficulty + "/*").sample
	end
	def getTestPath id
		return fileListed(id) ? @dbpath + "#{id[0]}/#{id[1]}/#{id[3..5]}/test.#{@ext[id[0]]}" : nil
	end
	def getProblemPath id
		return fileListed(id) ? @dbpath + "#{id[0]}/#{id[1]}/#{id[3..5]}/main.#{@ext[id[0]]}" : nil
	end
	def getHintsPath id
		return fileListed(id) ? @dbpath + "#{id[0]}/#{id[1]}/#{id[3..5]}/hints.txt" : nil
	end

end

class TestServer
	def initialize db
		@directories = ["test01","test02","test03","test04","test05","test06"]
		@db = db
		@logger = Logger.new "testServer.log"
	end

	def get_dir
		return "/home/test/" + @directories.pop
	end

	def return_dir dir
		@directories.insert(dir)
	end

	def log error
		@logger << error
	end

	#Tries a solution and returns the output
	def trySolution(id, code)
		output_file = "output.txt"
		test_path = @db.getTestPath(id)
		test_file = testpath.split('/').last
		solution_file = "solution." + testfile.split('.').last
		while (dir = get_dir).nil?
			sleep 5
		end
		err = %x(mkdir #{dir})
		if !err
			File.open("#{dir}/#{solution_file}",'w') {|f| f.write(code)}
			%x(cp #{test_path} #{dir}/)
			%x(#{dir}/#{test_file})

			response = ""
			File.open("#{dir}/#{output_file}", "r") {|f| response << f.read}

			%x(rm #{dir}/*)
			err = %x(rmdir #{dir})
			if !err
				return_dir(dir)
			else
				log(err)
			end
			return response
		end

	end
end

class ScorePlayerDB
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
if ARGV.empty?
	port = 8008
elsif ARGV.size == 1
	port = ARGV[0].to_i
else
	puts 'Usage: ServerCom.rb [port]'
	exit 1
end
#database = ProblemDB.new
#puts database.retrieveFile("p0p000")
if __FILE__ == $0
	Reception.new.run port
end

