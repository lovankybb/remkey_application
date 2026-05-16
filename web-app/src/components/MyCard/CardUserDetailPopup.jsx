import SmallExitHeader from "../Header/SmallExitHeader";
import defaultImg from "../../assets/image-regular-full.svg";

export default function CardUserDetailPopup({
  question,
  answer,
  language,
  topic,
  mainImage,
  close,
  isClose
}) {
  return (
    <div className={`card-user-detail ${isClose ? 'popup-out': 'popup-in'} `}>
      <SmallExitHeader onClick={close} />
      <div className="card-user-detail-content">
        <img
                  className="illustrate-img "
                  src={
                      mainImage
                      ? mainImage.url
                      : defaultImg
                  }
                  alt="Ảnh minh họa"
                />
        <p>
          <strong>Mặt trước: </strong>
          <p>{question}</p>
        </p>
        <p>
          <strong>Mặt sau: </strong>
          <p style={{"white-space": "pre-line"}}>{answer}</p>
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
