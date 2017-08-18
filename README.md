mvn compile exec:java -D"exec.mainClass=co.phystech.aosorio.app.Main"

mvn test

mvn test -Dtest=ControllerTest

deploy with:

git push heroku master
