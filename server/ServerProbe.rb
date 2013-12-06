require 'net/http'
require 'socket'
require 'uri'

include Socket::Constants

socket = Socket.new(AF_INET, SOCK_STREAM, 0)

sockaddr = Socket.pack_sockaddr_in(8008,'localhost')

socket.connect(sockaddr)

socket.write( "{'method':'randReq','difficulty':'2','language':'python33','id':'nil'}\r\n\r\n" )

results = socket.read

socket.close

socket = Socket.new(AF_INET, SOCK_STREAM, 0)

socket.connect(sockaddr)

socket.write( "{'method':'idReq','difficulty':'nil','language':'nil','id':'p1p001'}\r\n\r\n" )
#updated

results = socket.read

socket.close

socket = Socket.new(AF_INET, SOCK_STREAM, 0)

socket.connect(sockaddr)

socket.write( "{'method':'Post','id':'p1p001,'language':'python33','code':'def alpha(n)\n  return a'}\r\n\r\n" )
#updated

results = socket.read

socket.close

socket = Socket.new(AF_INET, SOCK_STREAM, 0)

socket.connect(sockaddr)

socket.write( "getScores\nplayer000001\r\n\r\n" )
#todo

results = socket.read

socket.close

socket = Socket.new(AF_INET, SOCK_STREAM, 0)

socket.connect(sockaddr)

socket.write( "getLeaderboard\nplayer000001\r\n\r\n" )
#todo

results = socket.read

socket.close

puts results

#Net::HTTP.get(URI('http://localhost:8008/probe'))

#Net::HTTP.post_form(URI('http://localhost:8008/probe'),{ "q" => "ruby", "max" => "50"})

