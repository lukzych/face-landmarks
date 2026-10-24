const fileInput = document.getElementById("fileInput");
const canvas    = document.getElementById("canvas");
const ctx       = canvas.getContext("2d");
const video     = document.getElementById("video");


fileInput.addEventListener("change", async () => {
  
  const file = fileInput.files[0];
  if (!file) return;

  showOnCanvas(file);

  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch("/api/upload", {

    method: "POST",
    body: formData
  });

  console.log(await response.text());
})

function showOnCanvas(file) {
  const img = new Image();
  img.onload = () => {
    canvas.width = img.naturalWidth;      // canvas dostaje prawdziwy rozmiar zdjęcia
    canvas.height = img.naturalHeight;
    ctx.drawImage(img, 0, 0);             // rysuje zdjęcie od lewego górnego rogu
    video.style.display = "none";         // chowa podgląd kamerki, jeśli był
    URL.revokeObjectURL(img.src);         // zwalnia pamięć po tymczasowym adresie
  };
  img.src = URL.createObjectURL(file);    // tymczasowy adres do pliku z dysku użytkownika
}