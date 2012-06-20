GetToken
=

GetToken allows users to request a token (the first 5 characters in a salted hash of their username) by using the /get-token command.

Configuration
-

The salt is taken from the automatically generated GetToken/config.yml file. The default salt is "NaCl", but it is strongly recommended that you change it to something secret immediately.
