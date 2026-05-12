import SmallExitHeader from "../Header/SmallExitHeader";
import defaultImg from "../../assets/image-regular-full.svg";

export default function CardUserDetailPopup({
  question,
  answer,
  language,
  topic,
  image,
  close,
  isClose
}) {
  return (
    <div className={`card-user-detail ${isClose ? 'popup-out': 'popup-in'} `}>
      <SmallExitHeader onClick={close} />
      <div className="card-user-detail-content">
        <p>
          <strong>Mặt trước: </strong>
          <span>{question}</span>
        </p>
        <p>
          <strong>Mặt sau: </strong>
          <span>{answer}</span>
        </p>
        <p>
          <strong>Ngôn ngữ: </strong>
          <span>{language}</span>
        </p>
        <p>
          <strong>Chủ đề: </strong>
          <span>{topic}</span>
        </p>
      </div>
    </div>
  );
}
