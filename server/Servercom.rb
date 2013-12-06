#!/usr/bin/env ruby

require 'socket'
require 'uri'


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
    request_line = from_client.readline
    problemDB = ProblemDB.new
    playerDB = ScorePlayerDB.new
    testplace = TestServer.new

    #convert request into function call by case
    #pProblem(id,code=string)
    #pSolution(id, bool, [achievements,..,..],response=str)
    #pScores(id,[uid,score=int])
    #pLeaderboard([uid,score=int])

    puts request_line

    scode = ""
    callargs = []
    response = ""

    case request_line.chomp
    #Posting a solution
    when "Post"
        callargs << from_client.readline
        loop do
          line = from_client.readline
          if line.strip.empty?
            break
          else
            scode << line
          end
        end

        response = testplace.pSolution(callargs,scode)
        #returns

    #Requesting a random problem of x difficulty & y language
    when "randReq"

        callargs << from_client.readline(sep=",")
        callargs[0][-1]=''
        callargs << from_client.readline

        response = problemDB.fetchRand(callargs[0],callargs[1])

    #Requesting a problem of ID
    when "idReq"
        callargs << from_client.readline

        response = problemDB.pProblem(callargs[0])

    #Fetch scores for client of ID
    when "getScores"
        callargs << from_client.readline

        response = playerDB.pScores(callargs[0])

    #Fetch leaderboard
    when "getLeaderboard"
        callargs = from_client.readline

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
  end
end

#testing placeholders
class ProblemDB
    def initialize
    end


    def fetchRand(diff,lang)
        puts "fetchRand found"
        #p[id,code] = pProblem(id)
        return ""
    end

    def pProblem(id)
        puts "pProblem found"
        return ""
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

Reception.new.run port
