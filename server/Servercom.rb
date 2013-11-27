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
    problemDB = PDB.new

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
    when "Post"
        loop do
          line = from_client.readline
          if line.strip.empty?
            break
          else
            scode << line
          end
        end

        response = problemDB.pSolution(callargs,scode)


    when "randReq"

        callargs << from_client.readline(sep=",")
        callargs[0][-1]=''
        callargs << from_client.readline

        response = problemDB.randReq(callargs[0],callargs[1])

    when "idReq"
        callargs = from_client.readline
    when "getScores"
        callargs = from_client.readline
    when "getLeaderboard"
        callargs = from_client.readline
    else
        puts "Failure to apprehend call"
    end

    #verb = request_line[/^\w+/]
    #url = request_line[/^\w+\s+(\S+)/, 1]
    buff = "\r\n\r\n"
    puts "responding"
    from_client.write(buff)
    puts "conn_end"
    # Close the sockets
    from_client.close
  end
end

class PDB
    def initialize
    end

    def pSolution(arg, code)
        puts "pSolution found"
        return ""
    end

    def randReq(diff,lang)
        puts "randReq found"
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
