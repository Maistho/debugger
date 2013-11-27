require 'net/http'
require 'socket'
require 'uri'

include Socket::Constants

socket = Socket.new(AF_INET, SOCK_STREAM, 0)

sockaddr = Socket.pack_sockaddr_in(8008,'localhost')

socket.connect(sockaddr)

socket.write( "randReq\n2,py\r\n\r\n" )
#socket.write( "Post\ndef alpha(n)\n    a=n*5\n    return a\nend\r\n\r\n" )

results = socket.read

socket.close

#Net::HTTP.get(URI('http://localhost:8008/probe'))

#Net::HTTP.post_form(URI('http://localhost:8008/probe'),{ "q" => "ruby", "max" => "50"})

