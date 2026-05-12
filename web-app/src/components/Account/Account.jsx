import { useState } from "react";
import Button from "../Button/Button";
import { AccountFeature } from "./AccountFeature";
import UserInfo from "./UserInfo";
import DisableLayer from "../Popup/DisableLayer";
import ContactTab from "./ContactTab";
import HelpCenterTab from "./HelpCenterTab";
import { getMyInfo, logout } from "../../service/UserService";

export default function Account() {
  const [user, setUser] = useState(null);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [enableUserInfo, setEnableUserInfo] = useState(false);
  const [isClose, setIsClose] = useState(false);
  const [enableContactTab, setEnableContactTab] = useState(false);
  const [enableHelpCenterTab, setEnableHelpCenterTab] = useState(false);

  // user's information tab
  async function openUserInfoTab() {
    setEnableDisableLayer(true);
    setEnableUserInfo(true);

    const data = await getMyInfo();
    setUser({
      id: data.body.id,
      username: data.body.username,
      email: data.body.email,
    });
  }

  async function handleLogout() {
    logout();
    window.location.href = "/login";
  }

  function closeUserInfoTab() {
    setIsClose(true);

    setTimeout(() => {
      setEnableUserInfo(false);
      setIsClose(false);
      setEnableDisableLayer(false);
    }, 150);
  }

  // contact tab
  function openContactTab() {
    setEnableDisableLayer(true);
    setEnableContactTab(true);
  }

  function closeContactTab() {
    setIsClose(true);

    setTimeout(() => {
      setEnableContactTab(false);
      setIsClose(false);
      setEnableDisableLayer(false);
    }, 150);
  }

  // help center tab
  function openHelpCenterTab() {
    setEnableDisableLayer(true);
    setEnableHelpCenterTab(true);
  }

  function closeHelpCenterTab() {
    setIsClose(true);

    setTimeout(() => {
      setEnableHelpCenterTab(false);
      setIsClose(false);
      setEnableDisableLayer(false);
    }, 150);
  }

  return (
    <>
      <div className="account-management-container">
        <h2>Account management</h2>
        <hr />
        <ul className="account-feater-container">
          <AccountFeature
            title="Thông tin tài khoản"
            onClick={openUserInfoTab}
          />
          <AccountFeature title="Liên hệ" onClick={openContactTab} />
          <AccountFeature
            title="Trung tâm trợ giúp"
            onClick={openHelpCenterTab}
          />
        </ul>
        <Button type="logout-btn" title="Đăng xuất" onClick={handleLogout} />
      </div>
      {enableDisableLayer && <DisableLayer />}
      {enableUserInfo && (
        <UserInfo exitBtn={closeUserInfoTab} isClose={isClose} {...user} />
      )}
      {enableContactTab && (
        <ContactTab
          exitBtn={closeContactTab}
          facebook="https://www.facebook.com/lovankydev/"
          zalo="https://zalo.me/0865305996"
          emai="https://mail.google.com/mail/?view=cm&fs=1&to=lovanky.work@gmail.com"
          isClose={isClose}
        />
      )}
      {enableHelpCenterTab && (
        <HelpCenterTab isClose={isClose} exitBtn={closeHelpCenterTab} />
      )}
    </>
  );
}
