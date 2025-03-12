# NeuroTracker

NeuroTracker é um aplicativo Android para acompanhamento de comportamentos e rotinas, especialmente útil para pessoas com condições neurodivergentes.

## Requisitos

- Android Studio Hedgehog | 2023.1.1
- JDK 17 ou superior
- Android SDK (mínimo API 24 - Android 7.0)
- Kotlin 1.9.0 ou superior
- Dispositivo Android ou Emulador

### Dependências Principais
- Jetpack Compose: 1.5.4
- Compose Material 3: 1.1.2
- Room: 2.6.1
- Koin: 3.5.0
- Navigation Compose: 2.7.5
- Kotlin Coroutines: 1.7.3
- Kotlin Flow: 1.7.3

## Configuração do Projeto

1. Clone o repositório:

bash
git clone https://github.com/seu-usuario/neurotracker.git
cd neurotracker

2. Abra o projeto no Android Studio
3. Sincronize o projeto com os arquivos Gradle
4. Execute Build -> Clean Project
5. Execute o aplicativo em um emulador ou dispositivo físico

## Arquitetura

O projeto utiliza a arquitetura MVVM (Model-View-ViewModel) com Clean Architecture, seguindo as melhores práticas do Android.

### Estrutura de Pacotes

com.example.neurotrack/
├── data/
│ ├── local/
│ │ ├── dao/ # Data Access Objects
│ │ ├── entity/ # Entidades do Room
│ │ └── AppDatabase # Configuração do Room
│ └── repository/ # Implementações dos repositórios
├── di/ # Injeção de dependência (Koin)
├── ui/
│ ├── components/ # Componentes reutilizáveis
│ ├── navigation/ # Configuração de navegação
│ ├── screens/ # Telas do aplicativo
│ ├── theme/ # Tema e estilos
│ └── viewmodels/ # ViewModels
└── utils/ # Utilitários e extensões

### Tecnologias Utilizadas

- **Jetpack Compose**: UI toolkit moderno para Android
- **Room**: Persistência de dados
- **Koin**: Injeção de dependência
- **Kotlin Coroutines & Flow**: Programação assíncrona
- **Navigation Component**: Navegação entre telas
- **Material Design 3**: Sistema de design

## Funcionalidades Planejadas

### 1. Home
- Visão geral dos comportamentos do dia
- Acesso rápido a registros
- Resumo das próximas atividades

### 2. Histórico
- Lista de registros de comportamentos
- Filtros por data e tipo
- Detalhes de cada registro

### 3. Adicionar
- Registro rápido de comportamentos
- Formulário para novos comportamentos
- Opções de categorização

### 4. Calendário
- Visualização mensal de registros
- Planejamento de atividades
- Lembretes e rotinas

### 5. Dashboard
- Análises e estatísticas
- Gráficos de progresso
- Insights sobre padrões

## Banco de Dados

### Entidades
1. **Behavior**
   - Comportamentos/atividades base
   - Atributos: nome, descrição, tipo

2. **BehaviorRecord**
   - Registros de ocorrências
   - Relacionado com Behavior
   - Inclui timestamp e notas

3. **Schedule**
   - Agendamentos e rotinas
   - Pode ser recorrente
   - Vinculado a Behaviors

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request
