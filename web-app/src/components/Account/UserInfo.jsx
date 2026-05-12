import { useState } from "react";
import ExitHeader from "../Header/ExitHeader";
import ChangePassword from "./ChangePassword";

function UserInfo({ username, email, id, exitBtn, isClose }) {
  const [enableChangePasswordTab, setEnableChangePasswordTab] = useState(false);

  function openChangePasswordTab() {
    setEnableChangePasswordTab(true);
  }
  const closeChangePasswordTab = () => {
    setEnableChangePasswordTab(false);
  };

  return (
    <>
      <div
        className={`userinfo-container ${isClose ? "popup-out" : "popup-in"}`}
      >
        <ExitHeader onClick={exitBtn} title="Thông tin tài khoản"/>
        <div>
          <p className="userinfo-item">
            <strong>ID: </strong>
            {id}
          </p>
          <p className="userinfo-item">
            <strong>Tên đăng nhập: </strong>
            {username}
          </p>
          <p className="userinfo-item">
            <strong>Email: </strong>
            {email}
          </p>
          <p className="userinfo-item change-password-btn" onClick={openChangePasswordTab}>
            Đổi mật khẩu
          </p>
        </div>
      </div>
      {enableChangePasswordTab && (
        <ChangePassword exitBtn={closeChangePasswordTab} />
      )}
    </>
  );
}

export default UserInfo;
