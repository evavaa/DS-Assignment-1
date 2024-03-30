## DS Assignment 1

### Architecture
1. TCP for communication
2. multi-threaded server implementing thread-per-connection architecture
3. multiple clients connect to a single server
4. proper exception handling

### Functions
#### Query the meaning(s) of a given word
The client should implement a function that is used to query the dictionary with the following
minimum (additional input/output parameters can be used as required) input and output:

Input: Word to search

Output: Meaning(s) of the word

Error: The client should clearly indicate if the word was not found or if an error occurred. In case
of an error, a suitable description of the error should be given to the user.

#### Add a new word
Add a new word and one or more of its meanings to the dictionary. For the word to be added
successfully it should not exist already in the dictionary. Also, attempting to add a word without
an associated meaning should result in an error. A new word added by one client should be visible
to all other clients of the dictionary server. The minimum input and output parameters are as
follows:

Input: Word to add, meaning(s)

Output: Status of the operation (e.g., success, duplicate)

Error: The user should be informed if any errors occurred while performing the operation.

#### Remove an existing word
Remove a word and all of its associated meanings from the dictionary. A word deleted by one
client should not be visible to any of the clients of the dictionary server. If the word does not exist
in the dictionary then no action should be taken. The minimum input and output parameters are
as follows:

Input: Word to remove

Output: Status of the operation (e.g., success, not found)

Error: The user should be informed if any errors occurred while performing the operation.

#### Update meaning of an existing word
Update associated meanings of a word from the dictionary. If multiple meanings already exist,
you need to add this new one to it (of course, if the same meaning already exists, then no need
to add). Update made by one client should be visible to any of the clients of the dictionary server.
If the word does not exist in the dictionary, then no action should be taken. The minimum input
and output parameters are as follows:

Input: Word to update, meaning(s)

Output: Status of the operation (e.g., success, not found)

Error: The user should be informed if any errors occurred while performing the operation.