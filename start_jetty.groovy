#!/usr/bin/env groovy

import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.*
import groovy.servlet.*
 
@Grab(group='org.mortbay.jetty', module='jetty-embedded', version='6.1.14')
def startJetty() {
    def jetty = new Server(8080)
     
    def context = new Context(jetty, '/', Context.SESSIONS)  
    context.resourceBase = '.'  
    context.addServlet(GroovyServlet, '*.groovy')  
     
    jetty.start()
}

print "Welcome to Billboard-Reader - http://localhost:8080/billboard.groovy" 
startJetty()
