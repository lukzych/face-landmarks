const canvas    = document.getElementById("canvas");
const ctx       = canvas.getContext("2d");
const video     = document.getElementById("video");
const detectBtn = document.getElementById("detectBtn"); 
const startBtn = document.getElementById("startBtn");
const snapBtn  = document.getElementById("snapBtn");
let stream = null;
let live = false;


let selectedFile = null;                                  


detectBtn.addEventListener("click", async () => {
  if (!selectedFile) return;

  const formData = new FormData();
  formData.append("file", selectedFile, "face.jpg");

  const response = await fetch("/api/detect", {
    method: "POST",
    body: formData
  });

  const result = await response.json();
  console.log(result);
  drawPoints(result);
});


function drawPoints(result) {
  const r = Math.max(3, canvas.width / 250);
  for (const face of result.faces) {
    ctx.strokeStyle = "lime";
    ctx.lineWidth = r / 2;
    ctx.strokeRect(face.x, face.y, face.w, face.h);

    ctx.fillStyle = "red";
    for (const p of face.points) {
      ctx.beginPath();
      ctx.arc(p.x, p.y, r, 0, Math.PI * 2);
      ctx.fill();
    }
  }
}

function guideBox() {
  const size = Math.round(canvas.height * 0.7);
  return {
    x: Math.round((canvas.width - size) / 2),
    y: Math.round((canvas.height - size) / 2),
    size
  };
}


startBtn.addEventListener("click", async () => {
  stream = await navigator.mediaDevices.getUserMedia({ video: true });
  video.style.display = "";
  video.srcObject = stream;
  await video.play();
  canvas.width = video.videoWidth;
  canvas.height = video.videoHeight;
  live = true;
  snapBtn.disabled = false;
  drawLive();
});


function drawLive() {
  if (!live) return;
  ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
  const g = guideBox();
  ctx.strokeStyle = "lime";
  ctx.lineWidth = 4;
  ctx.beginPath();
  ctx.ellipse(g.x + g.size / 2, g.y + g.size / 2, g.size * 0.38, g.size * 0.48, 0, 0, Math.PI * 2);
  ctx.stroke();
  requestAnimationFrame(drawLive);
}


snapBtn.addEventListener("click", () => {
  live = false;
  const g = guideBox();
  canvas.width = g.size;
  canvas.height = g.size;
  ctx.drawImage(video, g.x, g.y, g.size, g.size, 0, 0, g.size, g.size);
  stream.getTracks().forEach(t => t.stop());
  snapBtn.disabled = true;
  canvas.toBlob(blob => { selectedFile = blob; }, "image/jpeg", 0.95);
});