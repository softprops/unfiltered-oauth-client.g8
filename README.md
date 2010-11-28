# unfiltered oauth client

Example of a VERY SIMPLE client of a VERY SIMPLE unfiltered oauth server.

# usage

get and run the client

    g8 softprops/unfiltered-oauth-client
    cd unfiltered-oauth-client && sbt update run
    
get and run the server 
    g8 softprops/unfiltered-oauth-server
    cd unfiltered-oauth-server && sbt update run
    
# dependencies
  
   * unfiltered-filter - 0.2.2-SNAPSHOT
   * unfiltered-jetty - 0.2.2-SNAPSHOT
   * dispatch-oauth - 0.7.8-SNAPSHOT