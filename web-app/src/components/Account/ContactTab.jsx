import ExitHeader from "../Header/ExitHeader";
import emailLogo from "../../assets/mail-logo.png";
import zaloLogo from "../../assets/Logo-Zalo-Arc.webp"; 
import facebookLogo from "../../assets/Facebook_Logo_(2019).png.webp"; 


function ContactTab({ isClose, facebook, zalo, emai, exitBtn }) {
  return (
    <div className={`contact-container ${isClose ? "popup-out" : "popup-in"}`}>
      <ExitHeader onClick={exitBtn} title="Liên hệ" />
      <div className="contact-content">
        <a href={facebook}>
          <img src={facebookLogo} alt="" />
        </a>
        <a href={zalo}>
          <img src={zaloLogo} alt="" />
        </a>
        <a href={emai}>
          <img src={emailLogo} alt="" />
        </a>
      </div>
    </div>
  );
}

export default ContactTab; 