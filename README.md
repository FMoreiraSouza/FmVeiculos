<img src="media/Logo.png" alt="FMVeículos Logo"/>

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue?logo=kotlin)

---

## 📃 Descrição

O **FMVeículos** é um aplicativo Android desenvolvido em **Kotlin** para gerenciamento de uma concessionária de veículos. Ele permite que clientes visualizem veículos disponíveis, registrem interesses de compra, e que administradores gerenciem o estoque, confirmem ou cancelem interesses, e visualizem relatórios de vendas. O aplicativo utiliza o **Firebase** para autenticação, armazenamento de dados e imagens, garantindo uma experiência integrada e em tempo real. O projeto segue uma arquitetura **MVP (Model-View-Presenter)**, com separação clara de responsabilidades, facilitando a manutenção e escalabilidade. A interface é projetada para ser intuitiva, com suporte a navegação fluida e responsividade para diferentes tamanhos de tela.

---

## 💻 Tecnologias Utilizadas

- **Kotlin** → Linguagem de programação principal.
- **Android SDK** → Desenvolvimento nativo para Android.
- **Firebase** → Autenticação, Firestore (banco de dados), Storage (imagens).
- **Glide** → Carregamento e exibição de imagens.
- **AAChartModel** → Geração de gráficos para relatórios de vendas.
- **Coroutines** → Gerenciamento de operações assíncronas.
- **Navigation Component** → Navegação entre telas.
- **RecyclerView e GridView** → Exibição de listas e grades de dados.

---

## 🛎️ Funcionalidades

### 🔹 Autenticação

- Login e registro de usuários com validação de CPF.
- Recuperação de senha via e-mail.
- Diferenciação entre usuários administradores (@fmveiculos.com) e clientes.

### 🔹 Gerenciamento de Veículos

- **Clientes**:
  - Visualização do catálogo de veículos com imagens, preços e detalhes.
  - Registro de interesse em veículos.
  - Integração com WhatsApp para contato direto com a concessionária.
- **Administradores**:
  - Adição de novos veículos ao estoque com upload de imagens (galeria ou câmera).
  - Atualização de preço e quantidade de veículos.
  - Visualização do catálogo completo.

### 🔹 Gerenciamento de Interesses

- **Clientes**:
  - Visualização do histórico de interesses.
- **Administradores**:
  - Visualização, confirmação ou cancelamento de interesses pendentes.
  - Atualização automática do estoque ao confirmar interesses.

### 🔹 Relatórios

- Gráficos de interesses confirmados por mês, exibidos em uma interface interativa.

### 🔹 Navegação

- Navegação intuitiva com toolbar e drawer (clientes).
- Transições animadas entre telas.

### 🔹 Validações

- Máscara de CPF no cadastro.
- Validação de CPF utilizando algoritmo de dígitos verificadores.
- Verificação de campos obrigatórios em formulários.

---

## 📱 Responsividade

- **GridView** e **RecyclerView** adaptam-se a diferentes tamanhos de tela.
- Imagens carregadas com **Glide** ajustam-se automaticamente para otimizar performance.
- Layouts otimizados com **ConstraintLayout** para garantir consistência em dispositivos variados.

---

## ▶️ Como Rodar o Projeto

### Pré-requisitos

- **Android Studio** (versão recomendada: Koala ou mais recente).
- **JDK 17** ou superior.
- **Emulador Android** ou dispositivo físico com Android 5.0+.
- Conta no [Firebase](https://firebase.google.com/) configurada com o projeto.

### Clone o repositório

- git clone URL do projeto

### Sincronize o projeto no Android Studio

- Abra o **Android Studio**.
-  Selecione **File > Open** e escolha a pasta do projeto.
- Aguarde a sincronização do **Gradle**.

### Configure o Firebase

- Acesse o **Firebase Console**.
- Crie um novo projeto e adicione o aplicativo Android.
- Baixe o arquivo `google-services.json` e coloque-o na pasta `app/` do projeto.
- Habilite os serviços de **Authentication**, **Firestore** e **Storage** no Firebase Console.

### Instale as dependências

- No Android Studio, clique em **Sync Project with Gradle Files** para baixar as dependências.

### Emulador

- No Android Studio, abra o **Device Manager** (ícone de celular no canto superior direito).
- Crie um novo dispositivo virtual (recomendado: **Pixel 6 com API 33**).
- Inicie o emulador clicando no botão de **play**.

### Dispositivo físico

#### Via cabo USB

- Habilite o **Modo Desenvolvedor** no dispositivo (Configurações > Sobre o telefone > Toque 7 vezes no número da versão).
- Ative a **Depuração USB** em Configurações > Sistema > Opções do desenvolvedor.
- Conecte o dispositivo ao computador via cabo USB.

#### Via Wi-Fi

- No Android Studio, vá para **File > Settings > Appearance & Behavior > System Settings > Android SDK**.
- Instale o **Android 11 (R)** ou superior.
- No dispositivo, vá para **Opções do desenvolvedor > Depuração sem fio**.
- Use a opção **Pair device with QR code** ou **Pair device with pairing code** para conectar.
- Escaneie o QR code ou insira o código no Android Studio (View > Tool Windows > Device Manager > Pair Devices Using Wi-Fi).

## Rode o app

- Selecione o dispositivo/emulador no menu suspenso ao lado do botão **Run**.
- Clique em **Run** (ícone de play verde) ou use o comando:
   ```bash
   ./gradlew run
   
---

## 🎥 Apresentação do Aplicativo

Confira a apresentação do aplicativo em duas partes:  

- [Parte 1](https://youtu.be/jwXPy-maq4Q)  
- [Parte 2](https://youtu.be/EmIp1Qk3-Ik)
