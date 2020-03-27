import pandas as pd
import plotly.graph_objects as go

df1 = pd.read_csv('./data.csv')

fig = go.Figure()

fig.add_trace(go.Scatter(y=df1['output']))

# Update Random walks to: Random Walks QLearning/Random Toy/Simple-maze
fig.update_layout(title='OR - Single perceptron - learning',
                  plot_bgcolor='rgb(230, 230,230)')

fig.show()
