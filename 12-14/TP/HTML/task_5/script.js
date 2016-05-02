var c = document.getElementById("canvas");
ctx = canvas.getContext("2d");

function writeText(text) {
  ctx.fillStyle = "rgba(0,0,0,0.5)";
  ctx.fillRect(0,0,c.width,c.height);
  //Write text
  ctx.font = 'bold 30px sans-serif';
  ctx.setFillStyle = "rgba(0,0,0,1)";
  ctx.textBaseline = "middle";
  ctx.textAlign = "center";

  ctx.fillText(text, c.width/2, c.height/2);
}

var rectangles;

//Init
function initNewGame(){
  rectangles = new Array();
  for (var i=0; i<20; i++){
    rectangles.push(new rectangle());
  }
}

//Update mhetod
function repaint() {
  ctx.clearRect(0, 0, c.width, c.height);
  for (var i=0, l=rectangles.length; i<l ; i++)
    rectangles[i].draw();
}

//Check collisions ( O(n*n) )
function collisionDetected() {
  for (var i=rectangles.length-1; i>0; i--){
    for (var j=i-1; j>=0; j--){
      var a=rectangles[i], b=rectangles[j];
      if (a.x<=b.x+b.w && a.x+a.w>=b.x && a.y<=b.y+b.h && a.y+a.h>=b.y) {
        return true;
      }
    }
  }
  return false;
}

//Game process
function playGame(e) {
  counter++;
  var x = e.offsetX==undefined?e.layerX:e.offsetX;
  var y = e.offsetY==undefined?e.layerY:e.offsetY;
  for (var i=rectangles.length-1; i>=0; i--) {
    var a = rectangles[i];
    if (x > a.x && x <= a.x+a.w && y >= a.y && y <= a.y+a.h) {
      rectangles.splice(i,1);
      break;
    }
  }

  repaint();
  
}


//Rectangle obj
function rectangle() {
  this.h = Math.floor( Math.random()*100 + 50 );
  this.w = Math.floor( Math.random()*100 + 50 );

  this.x = Math.floor( Math.random() * ( c.width - this.w ));
  this.y = Math.floor( Math.random() * ( c.height - this.h ));

  this.getRndColor = function() {
    var r = Math.floor( 50+205*Math.random() ),
        g = Math.floor( 50+205*Math.random() ),
        b = Math.floor( 50+205*Math.random() );
    return 'rgb(' + r + ',' + g + ',' + b + ')';
  }
  
  this.color = this.getRndColor();

  this.draw = function() {
    ctx.fillStyle = this.color;
    ctx.fillRect(this.x, this.y, this.w, this.h);
  }
}

var rectangles;

//Init
function initNewGame(){
  counter = 0;
  startTime = Date.now();
  rectangles = new Array();
  for (var i=0; i<20; i++){
    rectangles.push(new rectangle());
  }
}

//Update mhetod
function repaint() {
  ctx.clearRect(0, 0, c.width, c.height);
  for (var i=0, l=rectangles.length; i<l ; i++)
    rectangles[i].draw();
}

//Check collisions ( O(n*n) )
function collisionDetected() {
  for (var i=rectangles.length-1; i>0; i--){
    for (var j=i-1; j>=0; j--){
      var a=rectangles[i], b=rectangles[j];
      if (a.x<=b.x+b.w && a.x+a.w>=b.x && a.y<=b.y+b.h && a.y+a.h>=b.y) {
        return true;
      }
    }
  }
  return false;
}

//Game process
function playGame(e) {
  counter++;
  var x = e.offsetX==undefined?e.layerX:e.offsetX;
  var y = e.offsetY==undefined?e.layerY:e.offsetY;
  for (var i=rectangles.length-1; i>=0; i--) {
    var a = rectangles[i];
    if (x > a.x && x <= a.x+a.w && y >= a.y && y <= a.y+a.h) {
      rectangles.splice(i,1);
      break;
    }
  }

  repaint();
  //If game is ower
  if (!collisionDetected()) {
    var time = (Date.now() - startTime)/1000;
    alert("Clicks: "+counter+
        "\nRectangles removed: "+(20-rectangles.length)+
        "\nTime: "+time+"sec"); 
    playing = false;

    //Win info
    writeText("Click to start new game");
  }
}

//Start game
function startGame() {
  playing = true;
  initNewGame();
  repaint();
  startTime = Date.now();
}

var counter = 0;
var collisionDetectedartTime;
var playing = false;

//Onclick event listener
c.onclick = function (e){
  if(!playing) 
    startGame();
  else playGame(e);
}

writeText("Click to start");
