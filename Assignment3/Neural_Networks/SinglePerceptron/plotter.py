import pandas as pd
import plotly.graph_objects as go

df1 = pd.read_csv('./data.csv', header=0)

fig = go.Figure()

fig.add_trace(go.Scatter(y=df1["outputs"]))

# Update Random walks to: Random Walks QLearning/Random Toy/Simple-maze
fig.update_layout(title='AND - Single perceptron - learning',
                  xaxis_title='Epoch',
                  yaxis_title='Error',
                  plot_bgcolor='rgb(230, 230,230)')

fig.show()
