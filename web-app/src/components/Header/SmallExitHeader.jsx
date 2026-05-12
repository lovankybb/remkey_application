import xSolid from '../../assets/x-solid-full.svg'

export default function SmallExitHeader({ onClick }) {
  return (
    <>
      <div className="small-exit-header">
        <img src={xSolid} onClick={onClick} />
      </div>
      <hr />
    </>
  );
}
