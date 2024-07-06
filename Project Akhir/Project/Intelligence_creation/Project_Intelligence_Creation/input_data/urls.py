from django.urls import path
from . import views
from django.conf.urls.static import static
from django.conf import settings

app_name = 'input_data' 

urlpatterns = [

    path('projects/', views.ProjectListCreateAPIView.as_view(), name='project_list_create'),
    path('projects/<int:pk>/', views.ProjectRetrieveUpdateDestroyAPIView.as_view(), name='project_detail'),

    path('', views.project_list, name='project_list'),
    path('projectss/create/', views.project_create, name='project_create'),
    path('projectss/<int:pk>/update/', views.project_update, name='project_update'),
    path('projectss/<int:pk>/delete/', views.project_delete, name='project_delete'),
    path('projectss/<int:pk>/', views.project_detail, name='project_detail'),

    path('datasets/', views.dataset_list, name='dataset_list'),
    path('datasets/new/', views.dataset_create, name='dataset_create'),
    path('datasets/<int:pk>/edit/', views.dataset_update, name='dataset_update'),
    path('datasets/<int:pk>/delete/', views.dataset_delete, name='dataset_delete'),
    path('datasets/<int:pk>/', views.dataset_detail, name='dataset_detail'),
    path('<int:pk>/add_column/', views.add_column, name='add_column'),
    path('edit_column/<int:pk>/', views.edit_column, name='edit_column'),
    path('delete_column/<int:pk>/', views.delete_column, name='delete_column'),

    path('proses/', views.proses_list, name='proses_list'),
    path('proses/create/', views.proses_create, name='proses_create'),
    path('proses/<int:pk>/update/', views.proses_update, name='proses_update'),
    path('proses/<int:pk>/delete/', views.proses_delete, name='proses_delete'),
    path('proses/<int:pk>/', views.proses_detail, name='proses_detail'),
    path('proses/<int:pk>/status/<str:status>/', views.proses_status_update, name='proses_status_update'),

    path('model-planning/', views.model_planning_list, name='model_planning_list'),
    path('model-planning/new/', views.model_planning_create, name='model_planning_create'),
    path('model-planning/<int:pk>/', views.model_planning_detail, name='model_planning_detail'),
    path('model-planning/<int:pk>/edit/', views.model_planning_update, name='model_planning_update'),
    path('model-planning/<int:pk>/delete/', views.model_planning_delete, name='model_planning_delete'),

    path('refining-planning/', views.refining_planning_list, name='refining_planning_list'),
    path('refining-planning/new/', views.refining_planning_create, name='refining_planning_create'),
    path('refining-planning/<int:pk>/', views.refining_planning_detail, name='refining_planning_detail'),
    path('refining-planning/<int:pk>/edit/', views.refining_planning_update, name='refining_planning_update'),
    path('refining-planning/<int:pk>/delete/', views.refining_planning_delete, name='refining_planning_delete'),

    path('training-testing-result/', views.training_testing_result_list, name='training_testing_result_list'),
    path('training-testing-result/new/', views.training_testing_result_create, name='training_testing_result_create'),
    path('training-testing-result/<int:pk>/', views.training_testing_result_detail, name='training_testing_result_detail'),
    path('training-testing-result/<int:pk>/edit/', views.training_testing_result_update, name='training_testing_result_update'),
    path('training-testing-result/<int:pk>/delete/', views.training_testing_result_delete, name='training_testing_result_delete'),

    path('refining-results/', views.refining_result_list, name='refining_result_list'),
    path('refining-results/new/', views.refining_result_create, name='refining_result_create'),
    path('refining-results/<int:pk>/', views.refining_result_detail, name='refining_result_detail'),
    path('refining-results/<int:pk>/edit/', views.refining_result_update, name='refining_result_update'),
    path('refining-results/<int:pk>/delete/', views.refining_result_delete, name='refining_result_delete'),

    path('komunikasi_manajemen/', views.komunikasi_manajemen_list,name='komunikasi_manajemen_list'),
    path('komunikasi_manajemen/create/', views.komunikasi_manajemen_create,name='komunikasi_manajemen_create'),
    path('komunikasi_manajemen/<int:pk>/', views.komunikasi_manajemen_detail, name='komunikasi_manajemen_detail'),
    path('komunikasi_manajemen/update/<int:pk>/update/', views.komunikasi_manajemen_update, name='komunikasi_manajemen_update'),
    path('komunikasi_manajemen/delete/<int:pk>/delete/', views.komunikasi_manajemen_delete, name='komunikasi_manajemen_delete'),

    path('datasetss/', views.DatasetListCreateAPIView.as_view(), name='dataset-list-create'),
    path('datasetss/<int:pk>/', views.DatasetRetrieveUpdateDestroyAPIView.as_view(), name='dataset-retrieve-update-destroy'),
    path('columnss/', views.ColumnListCreateAPIView.as_view(), name='column-list-create'),
    path('columnss/<int:pk>/', views.ColumnRetrieveUpdateDestroyAPIView.as_view(), name='column-retrieve-update-destroy'),

    path('view/', views.file_list, name='file_list'),
    path('apiprojects/', views.apiproject_list, name='apiproject_list'),

    path('training-testing-results/', views.TrainingTestingResultList.as_view(), name='training-testing-result-list'),
    path('training-testing-results/<int:pk>/', views.TrainingTestingResultDetail.as_view(), name='training-testing-result-detail'),
    path('komunikasi-manajemen/', views.KomunikasiManajemenListCreate.as_view(), name='komunikasi-manajemen-list-create'),
    path('komunikasi-manajemen/<int:pk>/', views.KomunikasiManajemenDetail.as_view(), name='komunikasi-manajemen-detail'), 
     path('fetch_data/', views.fetch_api_data, name='fetch_data'),
    path('update_status/', views.update_status, name='update_status'),
    path('status-form/', views.status_form_view, name='status-form'),
    path('project-info/', views.project_view, name='project-info'),
    ]


urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)