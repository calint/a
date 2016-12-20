USER=calint
curl "https://api.github.com/users/$USER/repos?per_page=1000" 2>/dev/null |
grep -o 'git@[^"]*' | awk -F/ '{print $2}' | awk -F. '{print $1}'
