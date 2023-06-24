import './styles.css';
import carImg from '../../assets/car.png';

export default function Cardcar() {
    return (
        <>
            <div className='dsct-cardcar-container dsct-cardcar-border'>
                <img src={carImg} alt="Carro" />
                <h3>Lorem ipsum dolor</h3>
            </div>
        </>
    );
}