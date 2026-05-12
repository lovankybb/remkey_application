import packageImg from "../../assets/money-check-dollar-solid-full.svg";
import accountImage from "../../assets/circle-user-solid-full.svg";
import homeImage from "../../assets/house-solid-full.svg";
import cardImage from "../../assets/rectangle-list-solid-full.svg";
import NavigationItem from "./NavigationItem";

const card = {
  title: "Thẻ của tôi",
  image: cardImage,
};



const servicePack = {
  title: "Gói dich vụ",
  image: packageImg,
};

const account = {
  title: "Tài khoản",
  image: accountImage,
};

const home = {
  title: "Trang chủ",
  image: homeImage,
};

export default function Header({
  openAccountTab,
  openHomeContentTab,
  openMyCardTab,
  openServicePackageTab,
  selectedNavigation,
  enableNotificationRedDot,
  setEnableNotificationRedDot,
}) {



  function hanldeOpenMyCardTab(){
    openMyCardTab();
    setEnableNotificationRedDot(false); 
  }

  return (
    <>
      <div className="header">
        <NavigationItem
          {...home}
          onClick={openHomeContentTab}
          selectedNavigation={
            selectedNavigation === "home-content-tab" ? true : false
          }
        />
        <NavigationItem
          {...card}
          onClick={hanldeOpenMyCardTab}
          selectedNavigation={
            selectedNavigation === "mycard-tab" ? true : false
          }
          enableRedDot={enableNotificationRedDot}
        />
        <NavigationItem
          onClick={openServicePackageTab}
          {...servicePack}
          selectedNavigation={
            selectedNavigation === "service-package-tab" ? true : false
          }
        />
        <NavigationItem
          {...account}
          onClick={openAccountTab}
          selectedNavigation={
            selectedNavigation === "account-tab" ? true : false
          }
        />
        
        
      </div>
     
    </>
  );
}
