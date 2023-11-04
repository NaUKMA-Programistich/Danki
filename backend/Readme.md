Build the jar file for the project using the fatJar task

Build a local docker image using this jar file (This is not necessary for deployment to fly.io, just a local check):

```bash
docker build -t danki_backend .
```

Deploying to fly.io is to be done by their instructions https://fly.io/docs/languages-and-frameworks/dockerfile/