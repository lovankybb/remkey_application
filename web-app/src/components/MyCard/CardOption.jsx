import xIcon from "../../assets/x-solid-full.svg";
import { checkRole } from "../../service/UserService";

function CardOptionItem({ title, onClick }) {
  return <li onClick={onClick}>{title}</li>;
}

export default function CardOption({
  openCreateCard,
  openTopicTab,
  openLanguageTab,
  openMyCardManagementTab,
  openReportTab,
  exitBtn,
}) {
  const role = checkRole();

  return (
    <div className="card-option slide-fade-in">
      <img onClick={exitBtn} src={xIcon} alt="" />
      <ul>
        <CardOptionItem title="Thẻ của tôi" onClick={openMyCardManagementTab} />
        <CardOptionItem title="Thêm thẻ mới" onClick={openCreateCard} />
        {role === "ADMIN" && (
          <CardOptionItem title="Quản lí chủ đề" onClick={openTopicTab} />
        )}
        {role === "ADMIN" && (
          <CardOptionItem title="Quản lí ngôn ngữ" onClick={openLanguageTab} />
        )}
        {role === "ADMIN" && (
          <CardOptionItem title="Report management" onClick={openReportTab} />
        )}
      </ul>
    </div>
  );
}
