import { EMDK } from 'capacitor-emdk';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    EMDK.echo({ value: inputValue })
}
