import React, { useState } from "react";
import "../App.css";
import axios from "axios";

function FileUploader() {
  const [texto, setTexto] = useState("");
  const [patron, setPatron] = useState("");
  const [resultado, setResultado] = useState(null);
  const [algoritmo, setAlgoritmo] = useState("kmp");
  const [errorMessage, setErrorMessage] = useState("");

  const handleFileChange = (event) => {
    const file = event.target.files[0];

    if (file && file.type === "text/plain") {
      const reader = new FileReader();
      reader.onload = (e) => {
        setTexto(e.target.result);
        setPatron("");
        setResultado(null);
        setErrorMessage("");
      };
      reader.readAsText(file);
    } else {
      setErrorMessage("Por favor, sube solo archivos de texto (.txt)");
      setTexto("");
      setPatron("");
      setResultado(null);
    }
  };

  const handleSearch = async () => {
    if (patron.trim() === "") {
      setErrorMessage("Por favor, ingresa un patrón para buscar.");
      return;
    }

    if (texto.trim() === "") {
      setErrorMessage("Por favor, carga un archivo de texto primero.");
      return;
    }

    try {
      const url = "http://localhost:8080/api/busqueda/buscar";

      const formData = new FormData();
      formData.append(
        "archivo",
        new Blob([texto], { type: "text/plain" }),
        "archivo.txt"
      );
      formData.append("patron", patron);
      formData.append("algoritmo", algoritmo);

      const response = await axios.post(url, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setResultado(response.data);
      setErrorMessage("");
    } catch (error) {
      setErrorMessage(
        "Error al realizar la búsqueda. Asegúrate de que el servidor esté corriendo."
      );
      console.error(
        "Error details:",
        error.response ? error.response.data : error
      );
    }
  };

  const resaltarCoincidencias = (texto, patron) => {
    if (!patron) return texto;

    const regex = new RegExp(`(${patron})`, "gi");
    const partes = texto.split(regex);

    return partes.map((parte, index) =>
      regex.test(parte) ? (
        <span key={index} style={{ backgroundColor: "yellow" }}>
          {parte}
        </span>
      ) : (
        parte
      )
    );
  };

  return (
    <div className="container">
      <div className="header">
        <h2>Subir archivo de texto</h2>
      </div>
      <input type="file" accept=".txt" onChange={handleFileChange} />
      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}

      {texto && (
        <>
          <textarea
            value={texto}
            readOnly
            style={{ width: "100%", height: "200px", marginBottom: "10px" }}
          />

          <div
            style={{
              width: "98.5%",
              height: "200px",
              overflow: "auto",
              border: "1.5px solid #ccc",
              padding: "10px",
              fontFamily: "monospace",
              whiteSpace: "pre-wrap",
              textAlign: "left",
            }}
          >
            {resultado &&
              resaltarCoincidencias(resultado.textoProcesado, patron)}
          </div>

          <div className="radio-group" style={{ marginBottom: "20px" }}>
            <label>
              <input
                type="radio"
                value="kmp"
                checked={algoritmo === "kmp"}
                onChange={(e) => setAlgoritmo(e.target.value)}
              />
              KMP
            </label>
            <label>
              <input
                type="radio"
                value="bm"
                checked={algoritmo === "bm"}
                onChange={(e) => setAlgoritmo(e.target.value)}
              />
              BM
            </label>
          </div>

          <div className="search-section">
            <input
              type="text"
              value={patron}
              onChange={(e) => setPatron(e.target.value)}
              placeholder="Buscar palabra"
            />
            <button onClick={handleSearch}>Buscar</button>
          </div>

          {resultado && (
            <p className="result-count">
              Total de coincidencias: {resultado.cantidad}
            </p>
          )}
        </>
      )}
    </div>
  );
}

export default FileUploader;
