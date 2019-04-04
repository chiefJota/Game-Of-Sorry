# Game-Of-Sorry
This is our software engineering final project implementing the game of Sorry!

Link to Sorry! rules: https://www.hasbro.com/common/instruct/Sorry.PDF 

Git with a Group Commands

To be used after you link your local repo to the project on GitLab.

    git pull origin master
    git checkout -b [YOUR_INITIALS]_[WORK_DESIGNATION]
    Do work. Check to see if it works.
    git add -A
    git commit -m "[COMMIT_MESSAGE]"
    git push origin [YOUR_INITIALS]_[WORK_DESIGNATION]
    On GitLab, send a merge request for your branch
    Wait for code approval.
    git checkout master
    git pull origin master

    //This is for deleting your branch...do this at the end, otherwise you
    //will have to create a new branch every time you do work
    git branch -D [YOUR_INITIALS]_[WORK_DESIGNATION]

    How to add project to github repository

    git init
    git add .
    git commit -m "my commit"
    git remote add origin git@github.com:username/repo.git
    git push origin master

    To untrack a single file that has already been added/initialized to your repository, i.e., stop tracking the file but not delete it          from your system use: git rm --cached filename
    To untrack every file that is now in your .gitignore:
    First commit any outstanding code changes, and then, run this command: 
    git rm -r --cached .
    This removes any changed files from the index(staging area), then just run: 
    git add .
    Commit it: 
    git commit -m ".gitignore is now working"
    To undo git rm --cached filename, use git add filename.
    Make sure to commit all your important changes before running git add . Otherwise, you will lose any changes to other files.
