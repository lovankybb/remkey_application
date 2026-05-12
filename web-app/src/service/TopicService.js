const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function createTopic(name) {
  const jwtToken = localStorage.getItem("jwtToken");
  const url = `${BASE_URL}/api/v1/remkey/topics`;

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
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

async function getAllTopics() {
  const jwtToken = localStorage.getItem("jwtToken");
  const url = `${BASE_URL}/api/v1/remkey/topics`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function getTopicById(topicId) {
  const jwtToken = localStorage.getItem("jwtToken");
  const url = `${BASE_URL}/api/v1/remkey/topics/${topicId}`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function deleteTopicById(topicId) {
  const jwtToken = localStorage.getItem("jwtToken");
  const url = `${BASE_URL}/api/v1/remkey/topics/${topicId}`;

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });
    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function updateTopic(topicId, newName) {
  const jwtToken = localStorage.getItem("jwtToken");
  const url = `${BASE_URL}/api/v1/remkey/topics/${topicId}`;

  try {
    const response = await fetch(url, {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
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

async function getTopicByName(name) {
  const jwtToken = localStorage.getItem("jwtToken");
  const url = `${BASE_URL}/api/v1/remkey/topics/find-by-name`;

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
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
  createTopic,
  getAllTopics,
  deleteTopicById,
  updateTopic,
  getTopicById,
  getTopicByName
};
