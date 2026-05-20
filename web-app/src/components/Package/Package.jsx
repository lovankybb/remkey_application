import PackageItem from "./PackageItem";
import PackageHeader from "./PackageHeader";

function Package() {
  return (
    <div className="service-package-container">
      <PackageHeader/>
      <PackageItem price={10000}/>
      <PackageItem price={100000}/>
      <PackageItem price={30000}/>
      <PackageItem price={10000}/>
    </div>
  );
}

export default Package;


