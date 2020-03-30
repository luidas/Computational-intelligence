import pandas as pd
import plotly.graph_objects as go

step = 10
df1 = pd.read_csv('accuracy.csv')
df = df1.groupby(df1.index // step).mean().to_frame()

fig = go.Figure()

fig.add_trace(go.Scatter(x=df['Accuracy'], y=df['Neurons']))


# Update Random walks to: Random Walks QLearning/Random Toy/Simple-maze
fig.update_layout(title='Accuracy versus Number of Neurons in Hidden Layer',
                   plot_bgcolor='rgb(230, 230,230)')

fig.show()
