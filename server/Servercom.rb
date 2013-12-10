#!/usr/bin/env ruby

require 'socket'
require 'uri'
require 'json'


class Reception
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
      puts 'Got Interrupt..'
      # Ensure that we release the socket on errors
    ensure
      if @socket
        @socket.close
        puts 'Socket closed..'
      end
      puts 'Quitting.'
    end
  end

  def handle_request from_client
    problemDB = ProblemDB.new
    playerDB = ScorePlayerDB.new
    testplace = TestServer.new

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


    jsoninc = JSON.parse(incoming)
    puts jsoninc

    response = ""

    case jsoninc['method']
    #Posting a solution
    when "Post"

        response = testplace.pSolution(jsoninc['id'],jsoninc['language'],jsoninc['code'])
        #returns

    #Requesting a random problem of x difficulty & y language
    when "randReq"

        response = fetchRand(jsoninc['language'],jsoninc['difficulty'])

    #Requesting a problem of ID
    when "idReq"

        response = problemDB.getProblem(jsoninc['id'])

    #Fetch scores for client of ID
    when "getScores"

        response = playerDB.pScores(callargs[0])

    #Fetch leaderboard
    when "getLeaderboard"
        response = playerDB.pLeaderboard(callargs[0])

    else
        puts "Failure to apprehend call"
    end

    buff = "placeholding\r\n\r\n"
    puts "responding"
    from_client.write(buff)
    puts "conn_end"
    # Close the sockets
    from_client.close
    return nil
  end

  def fetchRand
    #todo make a randomizer upon lang&diff
    return nil
  end
end

class ProblemDB
	#ID-standard: lang, difficulty, fileflag(Solution/Hint/Problem), 3-digit ID; Example: p1s001.
	#	The 3-digit ID is unique for each language and difficulty. Ie p1p000 and p2p000 are two separate problems.
    def initialize
		loadDB()
    end
	
	#Loads/reloads the database.
	def loadDB()
		@db = Hash.new
		lang = ""
		diff = 0
		File.open('dbroot/root.txt').each do |line|
			if line[0] != "#" && line[0] != "\t"
				lang = line[0]
				@db[lang] = []
			elsif line[0] == "\t" && line[1] != "\t"
				diff = line[1]
				@db[lang][diff.to_i] = []
			elsif line[0] == "\t" && line[1] == "\t"
				@db[lang][diff.to_i].push(line[2..4])
			end
		end
	end
	
	#Inspect the database Hash, mostly for error-shooting.
	def inspectDB()
		puts "Database: #@db"
	end
	
	#Checks if the file with the corresponding ID is listed in the database file. Unlisted but existing files are treated as non-existent until added to the database.
	def fileListed(id)
		i = 0
		fileFound = false
		for i in 0..@db[id[0]][1].size
			if @db[id[0]][id[1].to_i][i] == id[3..5]
				fileFound = true
			end
			i += 1
		end
		return fileFound
	end
	
	#Finds the file with the corresponding ID, given that it's listed in the database, and return it's content as a string.
	#	TODO: Dynamic file-ending handling, rather than a hardcoded solution.
	def retrieveFile(id)
		path = Dir.pwd
		if self.fileListed(id) == true
			if id[2] == "p"
				path = path + "/dbroot/#{id[0]}/#{id[1]}/#{id[3..5]}/main.py"
			elsif id[2] == "s"
				path = path + "/dbroot/#{id[0]}/#{id[1]}/#{id[3..5]}/solution.py"
			else
				path = path + "/dbroot/#{id[0]}/#{id[1]}/#{id[3..5]}/hints.txt"
			end
			fileContent = ""
			File.open(path).each do |line|
				fileContent += line
			end
			return fileContent
		else
			return nil
		end
	end
end

class TestServer
    def initialize
    end

    #posts a solution
    def pSolution(arg, code)
        puts "pSolution found"
        return ""
    end
end

#testing placeholders
class ScorePlayerDB
    def initialize
    end

    def pScores(id)
        puts "pScores found"
        return ""
    end

    def pLeaderboard(what)
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
database = ProblemDB.new
#puts database.retrieveFile("p0p000")
Reception.new.run port
