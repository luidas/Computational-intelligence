import pandas as pd
import plotly.graph_objects as go

# Was used to calculate and graph the data for finding the optimal number of neurons in the hidden layer.
# This class was used generally to create most of the graphs, this is just what was last used in this instance.
step = 10
df1 = pd.read_csv('accuracy.csv')
df = df1.groupby(df1.index // step).mean()

fig = go.Figure()

fig.add_trace(go.Scatter(x=df['Neurons'], y=df['Accuracy']))

fig.update_layout(title='Accuracy versus Number of Neurons in Hidden Layer',
                   plot_bgcolor='rgb(230, 230,230)')

fig.show()
