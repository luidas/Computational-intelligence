import pandas as pd
import plotly.graph_objects as go

df1 = pd.read_csv('C:\\Users\\Jbies\\OneDrive\\CSE\\CSE_Y2.2\\Q3\\Computational intelligent\\group-50\\plots\\adaptive_epsilon+gamma=0.1 0.csv')


fig = go.Figure()

fig.add_trace(go.Scatter(y = df1['avg']))


# Update Random walks to: Random Walks QLearning/Random Toy/Simple-maze
fig.update_layout(title='adaptive_epsilon+gamma=0.1',
                   plot_bgcolor='rgb(230, 230,230)')

fig.show()
