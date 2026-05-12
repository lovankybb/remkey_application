export default function NavigationItem({ title, image, onClick, selectedNavigation, enableRedDot}) {

  return (
    <div  onClick={onClick}  className={`navigation-item ${selectedNavigation ? "navigation-active": undefined}`}>
      <img src={image} alt={title}/>
   {enableRedDot &&<div className="red-dot"></div>}
    </div>
  );
}
