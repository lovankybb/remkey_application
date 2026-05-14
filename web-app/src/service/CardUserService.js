const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function getAllCardUsers() {
  const url = `${BASE_URL}/api/v1/remkey/my-cards`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function getAllMyStudyCards() {
  const url = `${BASE_URL}/api/v1/remkey/my-cards/study`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function deleteCardUser(id) {
  const url = `${BASE_URL}/api/v1/remkey/my-cards/${id}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function updateCardUser(id, question, answer, languageId, topicId) {
  console.log("card-user-id", id);
  const url = `${BASE_URL}/api/v1/remkey/my-cards/${id}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        question,
        answer,
        languageId,
        topicId,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function updateNextReviewTime(cardUserId, rating) {
  const url = `${BASE_URL}/api/v1/remkey/my-cards/update-next-review`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        cardUserId,
        rating,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function addCardToMyList(cardId) {
  const url = `${BASE_URL}/api/v1/remkey/my-cards/add`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        cardId,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function updateCardUserImage(imageId, newUrl, newPublicId) {
  const url = `${BASE_URL}/api/v1/remkey/my-cards/upload/${imageId}/update-image`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        url: newUrl,
        publicId: newPublicId,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

export {
  getAllCardUsers,
  getAllMyStudyCards,
  deleteCardUser,
  updateCardUser,
  updateNextReviewTime,
  addCardToMyList,
  updateCardUserImage,
};
