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

    #convert request into function call by case
    #pProblem(id,code=string)
    #pSolution(id, bool, [achievements,..,..],response=str)
    #pScores(id,[uid,score=int])
    #pLeaderboard([uid,score=int])

    #request_line = from_client.readline
    #verb = request_line[/^\w+/]
    #url = request_line[/^\w+\s+(\S+)/, 1]
    #version = request_line[/HTTP\/(1\.\d)\s*$/, 1]
    #uri = URI::parse url
    ## Show what got requested
    #puts((" %4s "%verb) + url)
    #puts(url)

    codebox = ''


    from_client.write('Inte inne')
    # Close the sockets
    from_client.close
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
