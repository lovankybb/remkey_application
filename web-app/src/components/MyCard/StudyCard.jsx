import { useEffect, useState } from "react";
import { getAllMyStudyCards } from "../../service/CardUserService";
import defaultImg from "../../assets/image-regular-full.svg";
import speakerIcon from "../../assets/volume-solid-full.svg";
import { speak } from "../../service/SoundService";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

const FlipCard = ({ question, answer, image, language }) => {
  const [isFlipped, setIsFlipped] = useState(false);

  const handleFlip = () => {
    setIsFlipped(!isFlipped);
  };

  const handleSpeak = ()=> {
     speak(question, language);
  }

  return (
    <div>
      <img onClick={handleSpeak} src={speakerIcon} alt="speaker" className="speaker-container" />
      <div className="flip-card-container" onClick={handleFlip}>
        <div className={`flip-card-inner ${isFlipped ? "is-flipped" : ""}`}>
          <div className="flip-card-front">
            <p>{question || "Click to see the question"}</p>
          </div>

          <div className="flip-card-back">
            <img
              src={
                image
                  ? BASE_URL + "/api/v1/remkey/files/images/" + image.url
                  : defaultImg
              }
            />

            <p className="study-card-answer">
              {answer || "This is the answer!"}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default function StudyCards({
  cards,
  setCards,
  index,
  isClose,
  setEnableRatingBtn,
}) {
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    const fetchStudyCards = async () => {
      try {
        const data = await getAllMyStudyCards();

        if (data.code === 1000) {
          setCards(data.body);
        }
      } catch (err) {
        console.log(err);
      } finally {
        setLoading(true);
      }
    };

    fetchStudyCards();
  }, []);

  if (cards.length === 0 && !loading) {
    setEnableRatingBtn(false);
    return <div className="study-card-container">Đang tải dữ liệu...</div>;
  }

  if (index >= cards.length || cards.length === 0) {
    setEnableRatingBtn(false);
    return <div className="study-card-container">Bạn đã ôn tập xong...</div>;
  }

  setEnableRatingBtn(true);
  return (
    <div
      className={`study-card-container ${isClose ? "popup-out-left" : "popup-in"}`}
    >
      <FlipCard
        question={cards[index].question}
        answer={cards[index].answer}
        image={cards[index].mainImage}
        language={cards[index].language}
      />
    </div>
  );
}
