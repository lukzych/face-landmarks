const fileInput = document.getElementById("fileInput");

fileInput.addEventListener("change", async () => {
  
  const file = fileInput.files[0];
  if (!file) return;


  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch("/api/upload", {

    method: "POST",
    body: formData
  });

  console.log(await response.text());
})