import Account from "../Account/Account";
import HomeContent from "../HomeContent/HomContent";
import MyCard from "../MyCard/MyCard";
import Package from "../Package/Package";

export default function MainContent({ selectedContent }) {
  switch (selectedContent) {
    case "home-content-tab":
      return <HomeContent />;
    case "account-tab":
      return <Account />;
    case "mycard-tab":
      return <MyCard />;
    case "service-package-tab":
      return <Package />;
    default:
      return null;
  }
}
