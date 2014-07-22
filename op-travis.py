#!/usr/bin/env python
import sys
import os
import os.path
import xml.dom.minidom

if os.environ["TRAVIS_SECURE_ENV_VARS"] == "false":
  print "no secure env vars available, skipping deployment"
  sys.exit()

homedir = os.path.expanduser("~")

m2 = xml.dom.minidom.parse(homedir + '/.m2/settings.xml')
settings = m2.getElementsByTagName("settings")[0]

serversNodes = settings.getElementsByTagName("servers")
if not serversNodes:
  serversNode = m2.createElement("servers")
  settings.appendChild(serversNode)
else:
  serversNode = serversNodes[0]

sonatypeServerNode = m2.createElement("server")
sonatypeServerId = m2.createElement("id")
sonatypeServerUser = m2.createElement("username")
sonatypeServerPass = m2.createElement("password")

idNode = m2.createTextNode("ossrh")
userNode = m2.createTextNode(os.environ["SONATYPE_USERNAME"])
passNode = m2.createTextNode(os.environ["SONATYPE_PASSWORD"])

sonatypeServerId.appendChild(idNode)
sonatypeServerUser.appendChild(userNode)
sonatypeServerPass.appendChild(passNode)

sonatypeServerNode.appendChild(sonatypeServerId)
sonatypeServerNode.appendChild(sonatypeServerUser)
sonatypeServerNode.appendChild(sonatypeServerPass)

serversNode.appendChild(sonatypeServerNode)

profilesNodes = settings.getElementsByTagName("profiles")
if not profilesNodes:
  profilesNode = m2.createElement("profiles")
  settings.appendChild(profilesNode)
else:
  profilesNode = profilesNodes[0]

profileNode = m2.createElement("profile")
idNode = m2.createElement("id")
idNode.appendChild(m2.createTextNode("ossrh"))

actNode = m2.createElement("activation")
actByDefNode = m2.createElement("activeByDefault")
actByDefNode.appendChild(m2.createTextNode("true"))
actNode.appendChild(actByDefNode)

propNode = m2.createElement("properties")

execNode = m2.createElement("gpg.executable")
execNode.appendChild(m2.createTextNode("gpg"))

passNode = m2.createElement("gpg.passphrase")
passNode.appendChild(m2.createTextNode(os.environ["GPG_DECRYPTION_KEY"]))

propNode.appendChild(execNode)
propNode.appendChild(passNode)

profileNode.appendChild(idNode)
profileNode.appendChild(actNode)
profileNode.appendChild(propNode)

profilesNode.appendChild(profileNode)

m2Str = m2.toxml()
f = open(homedir + '/.m2/mySettings.xml', 'w')
f.write(m2Str)
f.close()