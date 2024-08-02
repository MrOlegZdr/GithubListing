# GitHub Repositories Listing

Application provides a list of GitHub repositories that are NOT FORKS for the specified user.

## How it works

The servise based on [GitHub API](https://developer.github.com/v3). It receives username from the request and generate appropriate JSON-response.

### Request parametrs

* Request header: “Accept: application/json”
* Request method: GET
* {username} - the account owner of the repository. The name is not case sensitive.
```
http://localhost:8080/api/github/repositories/{username}
```
### Response format

* RepositoryName - The name of the repository
* OwnerLogin - Repository owner's login
* BranchName - Branch name
```
[
    {
        "name": "RepositoryName",
        "owner": {
            "login": "OwnerLogin"
        },
        "branches": [
            {
                "name": "BranchName",
                "commit": {
                    "sha": "5372e3093593a8f333bab8030c9a305df153d951"
                }
            }
        ]
    },
...
]
```
### Exception format
```
{
    "message": "User not found",
    "status": 404
}
```
## Used technology
* Java 21
* Spring Boot 3.3.2
