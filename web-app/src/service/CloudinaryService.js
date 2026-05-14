const CLOUD_NAME = import.meta.env.VITE_CLOUDINARY_CLOUD_NAME;
const PRESET_NAME = import.meta.env.VITE_CLOUDINARY_PRESET_NAME;

const uploadImageToCloud = async (image) => {
  const url = `https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`;

  const formData = new FormData();
  formData.append("file", image);
  formData.append("upload_preset", PRESET_NAME);
  try {
    const resp = await fetch(url, {
      method: "POST",
      body: formData,
    });
    return await resp.json();
  } catch (err) {
    console.log(err);
  }
};

export { uploadImageToCloud };
