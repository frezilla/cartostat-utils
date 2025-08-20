<a id="readme-top"></a>
# ReplaceLinkUtils

<details>
  <summary>Table des matières</summary>
  <ol>
    <li><a href="#about-the-project">A propos du projet</a></li>
    <li><a href="#built-with">Prérequis</a></li>
    <li><a href="#installation">Installation</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Feuille de route</a></li>
    <li><a href="#configuration">Configuration</a></li>
    <li><a href="#licence">Licence</a></li>
  </ol>
</details>

<a id="about-the-project"></a>
## A propos du projet

L'utilitaire ReplaceLinkUtils valorise les liens présents dans des fichiers SVG à partir d'un fichier de correspondance.

<p align="right">(<a href="#readme-top">retour</a>)</p></div>

<a id="built-with"></a>
## Prérequis

L'exécution de ce projet nécessite une machine virtuelle **java version 1.8**.

<p align="right">(<a href="#readme-top">retour</a>)</p>

<a id="installation"></a>
## Installation

<p align="right">(<a href="#readme-top">retour</a>)</p>

<a id="usage"></a>
## Usage

Les syntaxes d’usage, les plus utiles peuvent être :

```
Replaceutils.exe -srcdir <répertoire> -codefile <fichier.txt>

Avec  
-	<répertoire> qui est le chemin vers les fichiers SVG à traiter 
-	<fichier.txt>  qui est le chemin vers le fichiers des codes

Les fichiers SVG modifiés seront dans le même répertoire ; (les fichiers d’origines sont écrasés)
```

Ou pour conserver les fichiers d’origines :

```
Replaceutils.exe -srcdir <répertoire1> -codefile <fichier.txt> --outputdir <répertoire2>

Avec  
-	<répertoire> qui est le chemin vers les fichiers SVG à traiter 
-	<fichier.txt>  qui est le chemin vers le fichiers des codes
-	<répertoire2> qui est le répertoire dans lequel seront stockés les fichiers traités
```

Il est également possible d’accéder l’aide

```
Replaceutils.exe -help
```

Qui affiche l’aide suivante 
```
usage: ReplaceLink -srcdir <dir> -codefile <file>

Valorise les liens des fichier SVG depuis un fichier source des codes

-charset <name>    nom du jeu de caracteres
-codefile <file>   chemin vers le fichier des codes
-help              affiche ce message d'aide
-outputdir <dir>   chemin vers le repertoire de sortie
-srcdir <dir>      chemin vers le repertoire des fichiers a traiter
-verbose           active le mode verbeux
-version           affiche la version de l'utilitaire

version : 0.1.0
```

Il est par exemple possible de forcer le jeu de caractères à utiliser (qui est valorisé par défaut à windows-1252) ou d’activer le mode verbeux (qui permet d’afficher plus de messages au cours du traitement à des fin de deboggage)

<p align="right">(<a href="#readme-top">retour</a>)</p>

<a id="roadmap"></a>
## Feuille de route

### Version 0.1.0
<p align="right">(<a href="#readme-top">retour</a>)</p>

<a id="configuration"></a>
## Configuration

<p align="right">(<a href="#readme-top">retour</a>)</p>

<a id="licence"></a>
## Licence


<p align="right">(<a href="#readme-top">retour</a>)</p>
