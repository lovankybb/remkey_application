

const getPackageType = (price)=> {
    if(price < 20000)
        return 'bronze-package'; 
    else if (price < 50000)
        return 'silver-package'; 
    return 'gold-package';
}

const PackageItem = ({ price }) => {
  
    const packageType = getPackageType(price);

  return (
    <div className={`service-package-item ${packageType}`}>
      <h3>Premium60</h3>
      <p className="service-package-desc">
        Lorem ipsum dolor sit, amet consectetur adipisicing elit. Nemo, quod.
      </p>
      <p className="package-price">
        {price.toLocaleString()}<sup>vnđ</sup>
      </p>
      <button className="buy-now-btn">Mua ngay</button>
    </div>
  );
};

export default PackageItem;
