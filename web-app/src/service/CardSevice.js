const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function getAllCards() {
  const url = `${BASE_URL}/api/v1/remkey/cards`;;
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

async function createCard(question, answer, languageId, topicId) {
  const url = `${BASE_URL}/api/v1/remkey/cards`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        question: question,
        answer: answer,
        languageId: languageId,
        topicId: topicId,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function updateCard(cardId, question, answer, languageId, topicId) {
  const url = `${BASE_URL}/api/v1/remkey/my-cards/${cardId}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        question: question,
        answer: answer,
        languageId: languageId,
        topicId: topicId,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function deleteCard(cardId) {
  const url = `${BASE_URL}/api/v1/remkey/cards/${cardId}`;
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

async function getCardById(cardId) {
  const url = `${BASE_URL}/api/v1/remkey/cards/${cardId}`;
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

async function getNewestCards() {
  const url = `${BASE_URL}/api/v1/remkey/cards/explore`;
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

async function getNextCardsFromId(cardId) {
  const url = `${BASE_URL}/api/v1/remkey/cards/explore/${cardId}`;
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

async function updateCardImageNameOnServer(cardId, imageUrl, imagePublicId) {

  const url = `${BASE_URL}/api/v1/remkey/cards/${cardId}/upload`;
  const jwtToken = localStorage.getItem("jwtToken");


  try {
    const response = await fetch(url, {

      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        url: imageUrl, 
        publicId: imagePublicId
      })
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

export {
  getAllCards,
  getCardById,
  createCard,
  updateCard,
  deleteCard,
  getNewestCards,
  getNextCardsFromId,
  updateCardImageNameOnServer,
};
