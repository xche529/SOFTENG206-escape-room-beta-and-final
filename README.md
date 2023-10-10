# SOFTENG 206 - EscAIpe Room

## Acreditation

- outdoors.jpg <a href="https://www.freepik.com/free-vector/blank-landscape-nature-park-scene-with-many-pines_11206852.htm#page=2&query=cartoon%20outdoor%20background&position=17&from_view=keyword&track=ais">Image by brgfx</a> on Freepik


- paper.png <a href="https://www.freepik.com/free-vector/torn-paper-vintage-sticker-with-design-space-vector-set_20346116.htm#query=vintage%20torn%20paper&position=0&from_view=keyword&track=ais">Image by rawpixel.com</a> on Freepik

- MENU _screen.jpg <a href="https://www.freepik.com/free-vector/prison-corridor-with-two-empty-single-cells-steel-bars-cartoon_4997412.htm#query=prison%20cell&position=27&from_view=keyword&track=ais">Image by vectorpouch</a> on Freepik

- phone.jpg <a href = "https://www.freepik.com/free-vector/retro-landline-telephone-isolated_47755033.htm">Image by brgfx</a> on Freepik

- cross.png <a href = "https://www.freepik.com/free-vector/multiple-different-red-crosses_35514636.htm#query=multiple%20different%20red%20crosses&position=0&from_view=search&track=ais">Image by juivy_fish </a> on Freepik

-thoughtBubble.png <a href = "https://www.vecteezy.com/?utm_source=vecteezy-download&utm_medium=license-info-pdf&utm_campaign=license-info-document">

- thinking.gif https://tenor.com/view/emojis-emoji-hmm-thinking-think-gif-16078272

- cog.png <a href="https://www.freepik.com/free-vector/illustration-cogwheel_2609999.htm#query=settings%20icon&position=0&from_view=search&track=ais">Image by rawpixel.com</a> on Freepik

## To setup OpenAI's API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `apiproxy.config`
- put inside the credentials that you received from no-reply@digitaledu.ac.nz (put the quotes "")

  ```
  email: "upi123@aucklanduni.ac.nz"
  apiKey: "YOUR_KEY"
  ```
  these are your credentials to invoke the OpenAI GPT APIs

## To setup codestyle's API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `codestyle.config`
- put inside the credentials that you received from gradestyle@digitaledu.ac.nz (put the quotes "")

  ```
  email: "upi123@aucklanduni.ac.nz"
  accessToken: "YOUR_KEY"
  ```

 these are your credentials to invoke GradeStyle

## To run the game


`./mvnw clean javafx:run`

## To debug the game

`./mvnw clean javafx:run@debug` then in VS Code "Run & Debug", then run "Debug JavaFX"

## To run codestyle

`./mvnw clean compile exec:java@style`
