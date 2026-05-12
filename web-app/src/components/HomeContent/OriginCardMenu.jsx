import reportIcon from "../../assets/triangle-exclamation-solid-full.svg";
import infoIcon from "../../assets/circle-info-solid-full.svg";
import SmallExitHeader from "../Header/SmallExitHeader";

export default function OriginCardMenu({ language, topic, openReportPopup}) {
  return (
    <div className="origin-card-menu slide-fade-in">
      <div className="origin-card-menu-item">
        <img src={infoIcon} />
        <p>Thông tin khác</p>
      </div>
      <div className="origin-card-menu-section">
        <p>
          <strong>Ngôn ngữ: </strong>
          {language}
        </p>
        <p>
          <strong>Chủ đề: </strong>
          {topic}
        </p>
      </div>
      <div onClick={openReportPopup} className="origin-card-menu-item">
        <img src={reportIcon} />
        <p>Báo cáo</p>
      </div>
    </div>
  );
}
