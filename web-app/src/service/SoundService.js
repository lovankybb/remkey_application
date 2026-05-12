const speak = (question, language) => {
    let voice = "US English Female";

    switch (language) {
      case "English":
        voice = "US English Female";
        break;
      case "Chinese":
        voice = "Chinese Female";
        break;
      case "Deutsch":
        voice = "Deutsch Female";
        break;
      case "Korean": 
        voice = "Korean Male"; 
        break;
      case "Dutch": 
        voice = "Dutch Female"; 
        break;
      case "Japanese": 
        voice = "Japanese Female"; 
        break;
    }

    if (window.responsiveVoice) {
      window.responsiveVoice.speak(question, voice, {
        pitch: 0.8,
        rate: 0.8,
      });
    }
  };


  export {speak}; 