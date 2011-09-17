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
  
   * unfiltered-filter - 0.5.0
   * unfiltered-jetty - 0.5.0
   * unfiltered-json - 0.5.0
   * dispatch-oauth - 0.8.5
   * dispatch-json - 0.8.5
   * dispatch-lift-json - 0.8.5