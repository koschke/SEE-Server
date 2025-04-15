# SEE Managed Server

## Table of Contents
- [About this Project](#about-this-project)
- [Built with](#built-with)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Run Locally](#run-locally)
  - [Security Considerations](#security-considerations)
- [Deployment](#deployment)
- [Usage](#usage)
- [Documentation](#documentation)


## About this Project
This is the management server for [SEE](https://github.com/uni-bremen-agst/SEE), consisting of a backend and a frontend project.

The management server can be used to configure, run, and stop SEE game server instances.
Additionally, it provides an API to store and retrieve files for the use in multiple connected SEE clients.

## Built with

* [![React][React.js]][React-url]
* [![Vite][Vite]][Vite-url]
* [![Spring Boot][Springboot]][Springboot-url]
* [![Traefik][Traefik]][Traefik-url]


[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/

[Springboot]: https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&style=for-the-badge&logoColor=fff
[Springboot-url]: https://spring.io/projects/spring-boot

[Traefik]: https://img.shields.io/badge/Traefik-24A1C1?style=for-the-badge&logo=traefikproxy&logoColor=black
[Traefik-url]: https://traefik.io/

[Vite]: https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E
[Vite-url]: https://vitejs.dev/

## Getting Started

### Prerequisites
+ A Linux based machine (or WSL2)
+ Docker (or Podman) need to be installed (https://www.docker.com/)
+ (Optional) Install [just](https://github.com/casey/just)
  + You can view all available commands with:
```bash 
just
# or
just --list
```

### Environment Variables
To run this project, you will need to configure the following environment variables in the projects .env file.

| Variable | Description |
| -------- | ------- |
| DOMAIN_NAME | Domain under which the frontend/backend should be served |
| EXTERNAL_PORT | Port to expose the compose stack (default: 80) |
| DOCKER_HOST | Docker host address used to spawn new server instances |
| DOCKER_HOST_EXTERNAL | Public IPv4 address to register the game server |
| DOCKER_IMAGE_NAME | Docker image of the game server |
| JWT_SECRET | Secret used to sign auth tokens |
| JWT_EXPIRATION | Duration of token validity |
| INITIAL_ADMIN_USERNAME | Initial admin username |
| INITIAL_ADMIN_PASSWORD | Initial admin password |


You can generate a new random JWT secret using the following (requires OpenSSL):
```console
just seed .env
```
**Important:**
Make sure to update the JWT secret before using the server in production.

**Environment Setup Tips:**
+ DOMAIN_NAME must resolve to the machine hosting the server.
+ EXTERNAL_PORT must be accessible over the network.
+ DOCKER_HOST_EXTERNAL must be a valid, reachable IPv4 address.
+ For Podman, ensure DOCKER_HOST points to the socket (usually under /var/run/user/$UID/podman/podman.sock).

### Run Locally
See the README.md files in the Frontend and Backend directories for detailed local setup instructions.

#### Security Considerations

The backend – by default – has complete access to the Docker server it is running on.
Being able to launch any container means, it can potentially manipulate anything on the server.
If the backend itself is compromised, the complete server is also.

This can be mitigated by either:

- **Use a VM**: Running the backend or the complete stack in a VM.
- **Use a separate Docker host:** Giving the backend access to a separate Docker instance.
  - Set up a separate machine or VM to provide a Docker host for the game servers and configure the backend to use this instead.
-  **[Use Podman in rootless mode](https://wiki.archlinux.org/title/Podman#Rootless_Podman)**.
  - This will usually prevent the frontend to open port 80, which should be no problem as a reverse proxy with HTTPs should be used, anyway.

### Deployment
#### 1. Clone the project

```bash
git clone https://github.com/koschke/SEE-Server
cd SEE-Server
```

#### 2. Pull Docker Images
```bash
docker compose pull
# or using just
just pull-images
```

You also need to pull the actual SEE game server image:
```bash
$ docker pull ghcr.io/uni-bremen-agst/see-gameserver:1.0.0
```

#### 3. Start the Server

This project uses [Traefik](https://traefik.io/traefik/) as a reverse proxy. By default, it exposes services on port 80.

To enable SSL:
- Uncomment the relevant sections in compose.yaml
- Refer to [Traefik TLS Docs](https://doc.traefik.io/traefik/https/tls/)
- For automatic certs, use [Let's Encrypt](https://doc.traefik.io/traefik/https/acme/).

To start the server:
```console
docker compose up -d
# or
just start
```
Then visit the configured domain in your browser.

#### 4. Stop the Server

```bash
docker compose down
# or
just down
```

--------------------------------------------------------------------------------
## Usage

Using the Frontend, you can create and manage servers on the Backend.

When creating a new server, you can attach several Code City archives that are uploaded to the backend.
Each Code City type has a dedicated table in the virtual world.
Clients will automatically download the archives from the backend when connecting to the server instance and try to instantiate them on the appropriate table.


### Prepare Code Cities

The Code Cities are stored in the `Assets/StreamingAssets/Multiplayer/` directory.

**Heads up:** The `Multiplayer` directory will get cleared if you connect to a server. Keep that in mind while you prepare your Code Cities for upload!

Prepare your Code Cities depending on their type in the respective subdirectories:

- `SEECity`
- `DiffCity`
- `SEECityEvolution`
- `SEEJlgCity`
- `SEEReflexionCity`

This is necessary so that all relative paths are correctly referenced, e.g., in the `.cfg` files.

Each of the Code Cities should contain a valid configuration file (`.cfg`),
which is automatically loaded by the SEE clients after connecting to the server and downloading the files from the backend.

Each of the above Code City directories should be individually zipped.
You can either zip the directories' contents or the folder itself.
--------------------------------------------------------------------------------
## Documentation

Both subprojects have their own README file in their respective subfolders, which comes with additional information.

Keep the information collected here and in the READMEs of the subprojects up-to-date, and update them in a timely fashion, at the latest before merging the development branch back into the main branch.

Please feel free to add additional info whenever you change anything that is covered in this document, or whenever you overcome hurdles and would have liked to have found the information here in the first place.
