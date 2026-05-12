const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function createLanguage(name) {
  const url = `${BASE_URL}/api/v1/remkey/languages`;

  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: name,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function getAllLanguages() {
  const url = `${BASE_URL}/api/v1/remkey/languages`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
    });
    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function updateLanguage(languageId, newName) {
  const url = `${BASE_URL}/api/v1/remkey/languages/${languageId}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: newName,
      }),
    });
    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function deleteLanguage(languageId) {
  const url = `${BASE_URL}/api/v1/remkey/languages/${languageId}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
    });
    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function getLanguageById(languageId) {
  const url = `${BASE_URL}/api/v1/remkey/languages/${languageId}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
    });
    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function getLanguageByName(name) {
  const url = `${BASE_URL}/api/v1/remkey/languages/find-by-name`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name,
      }),
    });
    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

export {
  createLanguage,
  deleteLanguage,
  updateLanguage,
  getAllLanguages,
  getLanguageById,
  getLanguageByName
};
